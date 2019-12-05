(ns plzwork.views.center
  (:require [re-frame.core :as rf]
           [plzwork.subs :as subs]
           [plzwork.editor :as edi]))

(defn card [meta]
  [:div.card])

(defn all-cards []
  (let [card-metas @(rf/subscribe [::subs/all-card-metas])]
    [:div.all-cards
     (doall (for [meta card-metas]
              ^{:key (:id card)}
              [card meta]))]))

(defn center-panel []
  (let [center-content @(rf/subscribe [::subs/center-content])]
    (case center-content
      :editor [edi/editor-panel]
      :all-cards [all-cards])))
