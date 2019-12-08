(ns plzwork.views.center
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [plzwork.subs :as subs]
            [plzwork.editor :as edi]
            [plzwork.views.spotlight :as s]))


(defn dialog [class on-ok on-cancel lbl]
  (let [val (r/atom "")]
    (fn []
      [:div.modal {:class class}
       [:div.input
        [:label {:for lbl} lbl
         [:input.inp-txt {:name lbl :value @val :type "text" :on-change #(reset! val (-> % .-target .-value))}]]]
       [:button.cancel-btn {:on-click on-cancel} "CANCEL"]
       [:button.ok-btn {:on-click on-ok} "OK"]])))

(defn ref-dialog []
  (dialog "ref-dialog" #(println "KK") #(println ":G") "Link"))

(defn save-dialog []
  (dialog "save-dialog" #(println "KK") #(println ":X") "Tags"))

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
     [s/spotlight]
     (when (not= center-content :all-cards) [edi/editor-panel])
     (case center-content
       :all-cards [all-cards]
       :ref-dialog [ref-dialog]
       nil)]))
