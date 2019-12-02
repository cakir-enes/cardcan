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

(defn content [editor]
  (.save @editor))


(def  defaultValue (clj->js  [{:type "paragraph"
                               :children [{:text "A line of text in a paragraph."
                                           :marks []}]}]))

(defn editor-panel []
  (r/create-class
   {:component-did-mount (fn [this] () (reset! editor (make-editor "editor")))
    :reagent-render (fn [] [:div#editor])}))