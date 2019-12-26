(ns plzwork.storage
  (:require [konserve.memory :refer [new-mem-store]]
            [konserve.indexeddb :refer [new-indexeddb-store]]
            [cljs.core.async :refer [>! <!]]
            [konserve.core :as k]
            [clojure.core :refer [atom]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))


(go (def pdb (<! (new-indexeddb-store "konserve"))))


(defn gen-id [] 42)

(defn store-card [card]
  (go
    (k/assoc-in pdb [:cards :dude] card)
    (js/setTimeout #(go (println "DOESTI: " (<! (k/get-in pdb [:cards :dude])))) 100)))

; (defn remove-card-by-id [id]
;   (k/dissoc-in pdb id))