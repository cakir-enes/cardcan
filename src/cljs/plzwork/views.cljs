(ns plzwork.views
  (:require
   [re-frame.core :as rf]
   [plzwork.subs :as subs]
   [plzwork.editor :as edi]))


(defn tgl-btn [on-change]
  [:div
   [:input#cb3.tgl.tgl-skewed {:type "checkbox" :on-change on-change}]
   [:label.tgl-btn
    {:for "cb3", :data-tg-on "TO", :data-tg-off "FROM"}]])

(defn ref-menu [from?]
  [:div {:style {:display "flex" :margin-left "10px"}}
   [:div {:style {:transition "all 0.4s ease" :background "black" :transform (str "skew(-12deg) " (when @from? "rotateX(180deg)")) :border-radius "2px"  :width "8px" :height "2em"}}]
   [:h2 {:style {:margin "5px" :padding-bottom 2 :padding-right "5px"}} "REFS"]
   [tgl-btn #(swap! from? not)]])

(defn ref-list [from?]
  (let [refs (rf/subscribe [::subs/refs])]
    [:ul 
     (doall (for [ref ((if @from? :from :to) @refs)]
              ^{:key ref}
              [:li [:h4 (:id ref)]]))]))

(defn ref-view []
  (let [from? (reagent.core/atom true)]
    [:div {:style {:margin-top "0.50em"}}
     [ref-menu from?]
     [ref-list from?]]))


(defn main-panel []
  (let [name (rf/subscribe [::subs/name])]
    [:div
     {:style {

              :background-image "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)"
              ; :background "#3d3d3f"
              :display "grid" :grid-template-columns "1fr 4fr 1fr"
              :color "#3d3d3f"
              :border-radius "10px"
              :grid-column-gap "20px"}}
     [:div {:style {:border-right "2px solid #DEDEDA"}} [ref-view]]
     [edi/editor-panel]
     [:div {:style {:background ""}}]]))

