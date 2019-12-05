(ns plzwork.views.center
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [plzwork.subs :as subs]
            [plzwork.editor :as edi]))

(defn ref-dialog []
  (let [val (r/atom "")]
    (fn []
      [:div.modal.rev-dialog
       [:input#ref-txt {:value @val :type "text" :on-change #(reset! val (-> % .-target .-value))}]
       [:button.cancel-btn "CANCEL"]
       [:button.ok-btn "OK"]])))

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
    [:div.center-panel
     (when (not= center-content :all-cards) [edi/editor-panel]) 
     (case center-content
       :all-cards [all-cards]
       :ref-dialog [ref-dialog]
       nil)
     ]))
