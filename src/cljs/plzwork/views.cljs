(ns plzwork.views
  (:require
   [reagent.core :as r]
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

(def spotlight? (r/atom false))
(defn add-shortcuts! []
  (set! (.-onkeydown js/document)
        #(do (when (= (.-key %) "Escape") (reset! spotlight? false) ) (when (and (.-ctrlKey %) (= (.-key %) " ")) (swap! spotlight? not)))))

(defn ref-view []
  (let [from? (reagent.core/atom true)]
    [:div {:style {:margin-top "0.50em"}}
     [ref-menu from?]
     [ref-list from?]]))


(defn spotlight []
  )
(defn main-panel []    
  (add-shortcuts!)
  (fn []
    (let [name (rf/subscribe [::subs/name])]
      [:div
       {:style {:background-image "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)"
                ; :background "#3d3d3f"
                :display "grid" :grid-template-columns "1fr 4fr 1fr"
                :color "#3d3d3f"
                :border-radius "10px"}}
       [:div {:style {:border-right "2px solid #DEDEDA"}} [ref-view]]
       [:div {:style {:justify-content "center"}}
        [edi/editor-panel] 
       
        [:div#modal 
         {:style {
                  ; :display (when (not @spotlight?) "none")
                  :position "relative" :z-index 4 :bottom "70vh"   :width "100%" :height "100px"  :justify-content "center"}}
         [:div#command {:style {:display (when (not @spotlight?) "none")
                                :position "relative"
                                :margin-left "50px" :top "30px" :width "550px" :height "50px" :background "rgba(0, 21, 41, 0.97)"
                                :color "white"}}
          [:input {:auto-focus true
                   :placeholder "Command"
                   
                   :style {:font-size "1.7em" :width "100%" :height "100%"   :background "rgba(0, 21, 41, 0.97)"}}]
          [:ul [:li "ABC"] [:li "ZXC"]]]
          ]
         
        ]
       
       [:div {:style {:background ""}}]])))

