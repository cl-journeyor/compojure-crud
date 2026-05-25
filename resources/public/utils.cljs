(defn try-fetch-headers!
  [then catch]
  (-> (js/fetch "/api/token")
      (.then #(.json %))
      (.then then)
      (.catch catch)))

(defn js->form
  [jso]
  (let [form-data (js/FormData.)]
    (doseq [e (js/Object.entries jso)]
      (.append form-data (first e) (second e)))
    form-data))

(defn get-elem
  [id]
  (.getElementById js/document id))

(defn new-elem
  ([name]
   (.createElement js/document name))
  ([name txt]
   (let [elem (new-elem name)]
     (set! (.-textContent elem) txt)
     elem)))

(defn new-hidden
  [name value]
  (let [hidden (new-elem "input")]
    (set! (.-type hidden) "hidden")
    (set! (.-name hidden) name)
    (set! (.-value hidden) value)
    hidden))
