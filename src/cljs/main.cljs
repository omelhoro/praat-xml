(ns main
  (:use 
    [xmlconvert :only[convert-to-xml]]
    [domina.css :only[sel]]
    [domina :only[text set-text! value]]
    [domina.events :only[listen!]]
    [globals :only[sformat]]
    )
  )
 (enable-console-print!)

(def tg-field (sel ".textgrid"))
(def xml-field (sel ".xml"))
(def convert-button (sel ".convert"))
(defn convert [evt]
  (let [s (value tg-field)]
    (set-text! xml-field (convert-to-xml s))))
;(println (value tg-field))
;(def foo (filter #(not= "" %)  (clojure.string/split (value tg-field) #"(\r)*\n" )))
(println (convert-to-xml (value tg-field)))
;(println (count foo) foo (convert-to-xml foo))
(listen! convert-button :click convert) 
