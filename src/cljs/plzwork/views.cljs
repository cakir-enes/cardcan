(ns plzwork.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [plzwork.subs :as subs]
   [plzwork.editor :as edi]
   [plzwork.events :as events]
   [plzwork.views.header :as vh]))

(defn ref-list []
  (let [refs @(rf/subscribe [::subs/refs])]
    [:ul.ref-list 
     (doall (for [ref refs]
              ^{:key ref}
              [:li [:h4 (:id ref)]]))]))

(defn add-shortcuts! []
  (.addEventListener js/document "click"
                     #(if (= "cmd-txt" (-> % .-target .-id)) (rf/dispatch [::events/open-spotlight]) (rf/dispatch [::events/close-spotlight])))
  (set! (.-onkeydown js/document)
        #(do
           (when (= (.-key %) "Escape") (rf/dispatch [::events/shortcut-pressed :ESC]))
           (when (and (.-ctrlKey %) (= (.-key %) " ")) (rf/dispatch [::events/shortcut-pressed :CTRL_SPC])))))

(defn ref-view []
  [:div.header {:style {:margin-top "0.50em"}}
   [ref-list]])

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

(defn main-panel []
  (add-shortcuts!)
  (fn []
    [:div.container
     [vh/header]
     [ref-list]
     [center-panel]
     [:div.recent]]))

