(ns plzwork.views
  (:require
   [re-frame.core :as re-frame]
   [plzwork.subs :as subs]
   [plzwork.editor :as edi]))


(defn tgl-btn []
  [:div
   [:input#cb3.tgl.tgl-skewed {:type "checkbox"}]
   [:label.tgl-btn
    {:for "cb3", :data-tg-on "TO", :data-tg-off "FROM"}]])

(defn ref-view []
  [:div {:style {:display "flex" :margin-top "0.50em" :border-bottom "1px solid #DEDEDA"}}
   [:h2 {:style {:margin "5px" :padding-bottom 2 :padding-right "5px"}} "REFS"]
   [tgl-btn]])


(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
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

