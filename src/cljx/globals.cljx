(ns globals
 (:require
   #+clj [clojure.string]
#+cljs [goog.string :as gstring]  
          #+cljs [goog.string.format]
;          #+cljs [goog.string.trim]
))
(def ^:export sformat #+clj format #+cljs gstring/format)
