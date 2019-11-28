(ns plzwork.views
  (:require
   [re-frame.core :as re-frame]
   [plzwork.subs :as subs]
   [plzwork.editor :as edi]))


(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1 "Hello from " @name]
     [edi/editor-panel]]))
