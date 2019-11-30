(ns plzwork.views
  (:require
   [re-frame.core :as re-frame]
   [plzwork.subs :as subs]
   [plzwork.editor :as edi]))

(defn ref-view []
  [:div {:style {:background "blue"}}])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     {:style {:display "grid" :grid-template-columns "1fr 4fr 1fr"}}
     [ref-view]
     [edi/editor-panel]
     [:div {:style {:background "green"}}]]))

