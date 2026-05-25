(defn get-input
  [name]
  (.querySelector js/document (str "input[name='" name "']")))

(defn alert-error!
  [msg]
  (js/alert msg))

(defn consume-submit-result!
  [result]
  (if (= result "OK")
    (set! (.-href js/location) "../index.html")
    (alert-error! result)))

(defn fill-form!
  [params]
  (let [names ["id" "name" "role" "salary" "added-date"]]
    (doseq [name names]
      (set! (.-value (get-input name)) (.get params name)))))

(defn handle-insert!
  []
  (let [req (js-obj "name" (.-value (get-input "name"))
                    "role" (.-value (get-input "role"))
                    "salary" (.-value (get-input "salary")))]
    (try-fetch-headers!
     (fn [h]
       (-> (js/fetch "/api" (js-obj "method" "POST"
                                    "body" (js->form req)
                                    "headers" h))
           (.then #(.json %))
           (.then consume-submit-result!)
           (.catch alert-error!)))
     alert-error!)))

(defn handle-update!
  []
  (let [req (js-obj "id" (.-value (get-input "id"))
                    "name" (.-value (get-input "name"))
                    "role" (.-value (get-input "role"))
                    "salary" (.-value (get-input "salary"))
                    "added_date" (.-value (get-input "added-date")))]
    (try-fetch-headers!
     (fn [h]
       (-> (js/fetch "/api" (js-obj "method" "PUT"
                                    "body" (js->form req)
                                    "headers" h))
           (.then #(.json %))
           (.then consume-submit-result!)
           (.catch alert-error!)))
     alert-error!)))

(defn load-app!
  []
  (let [params (-> js/location
                   .-search
                   js/URLSearchParams.)
        id (.get params "id")]
    (set!
     (.-textContent (get-elem "title"))
     (if id (str "Update employee " id) "Insert employee"))
    (.addEventListener
     (get-elem "form")
     "submit"
     (fn [e]
       (.preventDefault e)
       (if id (handle-update!) (handle-insert!))))
    (when id (fill-form! params))))

(load-app!)
