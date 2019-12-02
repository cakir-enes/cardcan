(ns plzwork.views
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [plzwork.subs :as subs]
   [plzwork.editor :as edi]
   [plzwork.events :as events]))


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
  (.addEventListener js/document "click" #(reset! spotlight? false))
  (set! (.-onkeydown js/document)
        #(do
           (when (= (.-key %) "Escape")
             (do
               (reset! spotlight? false)
               (rf/dispatch [::events/focus-editor])))
           (when (and (.-ctrlKey %) (= (.-key %) " "))
             (do
               (if @spotlight? (rf/dispatch [::events/focus-editor]) (.focus (.getElementById js/document "cmd-txt")))
               (swap! spotlight? not))))))

(defn ref-view []
  (let [from? (reagent.core/atom true)]
    [:div.header {:style {:margin-top "0.50em"}}
     [ref-menu from?]
     [ref-list from?]]))

(defn action [main opts shortcut]
  [:div.cmd [:span.main main] [:span.opts opts] [:span.shortcut shortcut]])

(defn spotlight []
  [:div
   [:div {:class (str "buscar-caja" (when @spotlight? "-hovered"))}
    [:input.buscar-txt {:id "cmd-txt" :type "text" :auto-focus true :placeholder "Buscar..."}]
    [:a.buscar-btn
     [:i {:class "fa fa-hand-rock"}]]]
   [:ul.cmd-list {:style {:display (when (not @spotlight?) "none")}}
    [action "Open" "card" "o c"]
    [action "Send" "inbox | todos" "s [i t]"]
    [action "Query" "" "q"]
    [action "Reference" "card | page" "r [c p]"]]])


(defn main-panel []    
  (add-shortcuts!)
  (fn []
    (let [name (rf/subscribe [::subs/name])]
      [:div.container
       [:div.refs [ref-view]]
       [edi/editor-panel]
       [:div.cmd-container 
        [spotlight]
        ; [:div.dash]
        ]
      ;  [:div {:class (str "search" (when @spotlight? " open"))}
        
        ; [:input.search-box {:auto-focus true
        ;                     :placeholder "Command"
        ;                     :type "search"}]
        ; [:span.search-button [:span
        ;                       {:class (str "search-icon" (when @spotlight? " open"))}]]
        ; ]
      ])))

