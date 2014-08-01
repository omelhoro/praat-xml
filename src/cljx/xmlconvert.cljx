(ns xmlconvert
 (:require [clojure.string] )
 (:use [globals :only [sformat]]
; (:use []))
))

(def ++ clojure.string/join)

(def i0 {:tag "interval" :attrs {:xmin 8 :name 12} :content ["Vowel A"]})
(def t0 {:tag "tier" :attrs {:class "tier" :name "jane"} :content (seq '(i0 i0))})
(def root {:tag "recording" :attrs {:xmin 0 :xmax 20} :content [t0 t0]})


(def foo "
File type = 'ooTextFile'
Object class = 'TextGrid'

xmin = 0 
xmax = 127.33823129251701 
tiers? <exists> 
size = 4 
item []: 
    item [1]:
        class = 'IntervalTier' 
        name = 'Silben' 
        xmin = 0 
        xmax = 127.33823129251701 
        intervals: size = 468 
        intervals [1]:
            xmin = 0 
            xmax = 5.489327034517511 
            text = ''
        intervals [2]:
            xmin = 5.489327034517511 
            xmax = 5.571182917611489 
            text = 's' ")

(def ss (filter #(not= "" %)  (clojure.string/split foo #"\n" )))
(def level-of-grids {0 "recording" 4 "tier" 8 "interval"})
(def erroneos (set ["item_[]_" "tiers?_<exists>"]))
(defn startsWith [s check]
  (= (.substring s 0 (count check)) check))
(defn endsWith [s check]
  (do ;(println s check (.substring s (- (count s) (inc (count check)))) (= (.substring s (- (count s) (inc (count check)))) check))
  (= (.substring s (- (count s)  (count check))) check))) 

(defn wrap-s [s wraps]
  (if (startsWith s wraps) 
    s
 (sformat "%s%s%s" wraps s wraps)))

(defn make-map [ls]
  (do ;(println (filter #(and (not= "" %) (not (nil? %))) ls))
    (assoc (reduce (fn [m s] (let [[k v] 
                                 (clojure.string/split (clojure.string/trim s) #" = ")
                                 k (clojure.string/replace k #"[: ]" "_") ]
                               (do 
                             (cond
                               (contains? erroneos k) m
                               (= "text" k) (assoc m :content [v])
                               (endsWith k "]_") (assoc-in m [:attrs "i"] (wrap-s (second (re-find #"\[(\d+)\]" k)) "\""))
                                 :else  (assoc-in m [:attrs k] (wrap-s v "\""))))))
                 {} ls) :tag (get level-of-grids (count (take-while #(= % \space) (first ls))) "unk"))))

(defn map-xml-objs [s] 
  (let [splitted-s (filter #(and (not= "" %) (not (nil? %)))  (clojure.string/split s #"(\r)*\n" ))
        header (make-map (take 7 splitted-s))
        body (do ;(println splitted-s)  
               (drop 7 splitted-s))]
    (cons header (map make-map 
                                   (reduce (fn [v r] (let [s (do (clojure.string/trim r))] 
                                                                        (if  (endsWith s ":") 
                                                                          (conj v [r]) 
                                                                          (do (update-in v [(dec (count v))] #(conj % r)))))) [] body)))))

(defn nest-contents [ls]
           (reduce (fn [m k] (let [{t :tag} k] (if (= t "tier") 
                                                (update-in m [:content] #(conj % k)) 
                                                (update-in m [:content (- (count (get m :content)) 1) ] (fn [v] (update-in v [:content] #(if (nil? %) [k] (conj % k))))
                                                )))) (assoc (first ls) :content []) (rest ls)))  
(defn xmlc [i-level el]
  (let [
        {t :tag} el
        ind (sformat (++ [\newline \% i-level \s]) " ")
        tags (sformat "<%s %s>%s" t (++ " " (map (fn [m] (let [[k v] m] (sformat "%s=%s" k v))) (get el :attrs))) ind)
        ]
  (sformat "%s%s%s</%s>" tags 
          (++ ind (map
                    (fn [el](do (if (or (string? el) (number? el)) (str el) (xmlc (+ i-level 4) el))))
                    (get el :content)))
          ind t)))

(defn convert-to-xml [s]
  (reduce #(%2 %1) s [map-xml-objs nest-contents (partial xmlc 1)]))