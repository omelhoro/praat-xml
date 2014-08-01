(ns main 
  (:use [xmlconvert :only [convert-to-xml]])
  (:gen-class))

(defn write-xml [p s]
  (with-open [wrtr (clojure.java.io/writer (clojure.string/replace p #"\.\w+$" ".xml"))]
    (.write wrtr s)))
(defn read-convert-dir [p]  
  (dorun (map #(do (println (.getPath %))  (write-xml (.getPath %) (convert-to-xml (slurp %))))
              (filter #(.endsWith (.toLowerCase (.getName %)) ".textgrid")
                      (file-seq (clojure.java.io/file p))))))

(defn -main  [ & args]
  (let [[p & rest] args ]
    (do
    (println args p (filter #(.endsWith (.toLowerCase (.getName %)) ".textgrid")
               (file-seq (clojure.java.io/file p))))
         (read-convert-dir p))))

;(convert-to-xml (slurp (first (file-seq (clojure.java.io/file "target/test/112163_ru_06_k.TextGrid")))))
;(clojure.string/replace (.getPath (first (file-seq (clojure.java.io/file "target/test/112163_ru_06_k.TextGrid")))) #"\.\w+$" ".xml") 
