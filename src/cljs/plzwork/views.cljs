(ns plzwork.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [plzwork.subs :as subs]
   [plzwork.views.center :as c]
   [plzwork.events :as events]
   [plzwork.views.header :as vh]
   [plzwork.views.reflist :as rl]))

; (defn ref-list []
;   (let [refs @(rf/subscribe [::subs/refs])]
;     [:ul.ref-list 
;      (doall (for [ref refs]
;               ^{:key ref}
;               [:li [:h4 (:id ref)]]))]))

(defn ref-list []
  (let [refs @(rf/subscribe [::subs/refs])]
    [:ul.ref-list
     (doall
      (map-indexed (fn [i ref]
                     ^{:key ref}
                     [:li [:h4 (str i " " (:id ref))]])
                   refs)
      ; (for [ref refs]
      ;   ^{:key ref}
      ;   [:li [:h4 (:id ref)]])
      )]))



(defn add-shortcuts! []
  (.addEventListener js/document "click"
                     #(if (= "cmd-txt" (-> % .-target .-id)) (rf/dispatch [::events/open-spotlight]) (rf/dispatch [::events/close-spotlight])))
  (set! (.-onkeydown js/document)
        #(do
           (let [pressed? (fn [k] (= (.-key %) k))]
             (when (pressed? "Escape") (rf/dispatch [::events/shortcut-pressed :ESC]))
             (when (and (.-ctrlKey %) (pressed? " ")) (rf/dispatch [::events/shortcut-pressed :CTRL-SPC]))
             (when (and (.-ctrlKey %) (pressed? "s")) (rf/dispatch [::events/shortcut-pressed :CTRL-S]))))))

(defn ref-view []
  [:div.header {:style {:margin-top "0.50em"}}
   [ref-list]])

(defn right-sidebar []
   [:div.right-sidebar
    [rl/tags-list]
    [:div.divider [:h4 "PICK"]]
    [:div [:h2 "ASD"]]
    ; [rl/recent-cards-list]
    ])

(defn main-panel []
  (add-shortcuts!)
  (fn []
    [:div.container
     [vh/header]
     [rl/ref-list]
     [c/center-panel]
     [right-sidebar]]))

