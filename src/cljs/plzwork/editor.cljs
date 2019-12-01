(ns plzwork.editor
  (:require
   [reagent.core :as r]
   ["@editorjs/editorjs" :default EditorJS :as ed]
   ["@editorjs/header" :default Header :as header]
   ["@editorjs/list" :default List :as lists]
   ["prosemirror-state" :refer (EditorState)]
   ["prosemirror-view" :refer (EditorView)]
   ["prosemirror-model" :refer (Schema, DOMParser)]
   ["prosemirror-schema-basic" :refer (schema)]
   ["prosemirror-example-setup" :refer (exampleSetup)]
   ["prosemirror-schema-list" :refer (addListNodes)]
   ["quill" :default Quill :as Q]
   ["slate" :refer (createEditor)]
   ["slate-react" :refer (Slate, Editable, withReact)]

   [reagent.core :as r]))

(defonce editor (r/atom nil))

(defn make-editor [div-id]
  (ed. (clj->js {:holder div-id
                 :tools {:header {:class header :inlineToolbar ["link"]}
                         :list {:class lists :inlineToolbar true}}})))

; (defn content [editor]
;   (.save @editor))



; (def mySchema (Schema. 
;                (clj->js {
;                          :nodes (-> schema .-spec .-nodes)
;                         ;  :nodes (addListNodes (.-nodes (.-spec schema)) "paragraph block*" "block")
;                          :marks (.-marks (.-spec schema))})))


; (defn make-editor [div-id]
;   (EditorView. (clj->js 
;                  {:doc (.parse ((-> DOMParser .-fromSchema) mySchema) (js/document.querySelector div-id))
;                   :plugins (exampleSetup (clj->js {:schema mySchema}))})))

(def opts #js {:theme "snow"})
(defn ->editor [div-id]
  (let [counter (fn [quill] (.-length (.split (.getText quill) #"\s+")))
        register (.-register Q)]
    (do
      (register "modules/counter" 
                 (fn [quill opt] 
                   (let [container (js/document.querySelector "#counter")]
                     (set! (.-innerText container) "ZXC")))))
    (println "MODULES " (js->clj Q))
    (Q. (js/document.getElementById div-id) opts)))



(def  defaultValue (clj->js  [
                              {
                               :type "paragraph"
                               :children [
                                          {
                                           :text "A line of text in a paragraph."
                                           :marks []
                                           },
                                          ],
                               },
                              ]))


(defn codeElem [props]
  [:pre  (.-attributes props) [:code (.-children props)]])

(defn default [props]
  [:p (merge nil (:attributes props)) (:children props)])

(defn renderElem [props]
  (r/create-element 
   (r/reactify-component 
    (if (= (-> props .-element .-type) "code") codeElem default))
   props))

; (defn editor-panel []
;   (let [ed (withReact (createEditor))]
;     (fn []
;       [:div#editor 
;        [:> Slate {:editor ed :defaultValue defaultValue}
;         [:> Editable 
;          {:onKeyDown #(println (.-key %))
;           :renderElement renderElem}]]])))

(defn editor-panel []
  (r/create-class
   {:component-did-mount (fn [this] () (reset! editor (make-editor "codexz")))
    :reagent-render (fn [] [:div [:div#codexz {:style {}}] [:div#counter 0]])}))

; (defn editor-panel [] [:div#codexz])





