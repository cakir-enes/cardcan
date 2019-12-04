(ns plzwork.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [plzwork.subs :as subs]
   [plzwork.editor :as edi]
   [plzwork.events :as events]
   [plzwork.views.header :as vh]))

(defn ref-list []
  (let [refs (rf/subscribe [::subs/refs])]
    [:ul.ref-list 
     (doall (for [ref @refs]
              ^{:key ref}
              [:li [:h4 (:id ref)]]))]))



(defn add-shortcuts! []
  (.addEventListener js/document "click" #(rf/dispatch [::events/close-spotlight]))
  (set! (.-onkeydown js/document)
        #(do
           (when (= (.-key %) "Escape") (rf/dispatch [::events/shortcut-pressed :ESC]))
           (when (and (.-ctrlKey %) (= (.-key %) " ")) (rf/dispatch [::events/shortcut-pressed :CTRL_SPC])))))

(defn ref-view []
  (let [from? (reagent.core/atom true)]
    [:div.header {:style {:margin-top "0.50em"}}
     [ref-list from?]]))


(defn main-panel []    
  (add-shortcuts!)
  (fn []
    [:div.container
     [vh/header]
     [ref-list]
     [edi/editor-panel]
     [:div.recent]]))

