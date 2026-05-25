(defn get-page-number
  []
  (let [unsafe-page-number (-> js/location
                               .-search
                               js/URLSearchParams.
                               (.get "page")
                               js/parseInt)]
    (if (js/isNaN unsafe-page-number)
      1
      unsafe-page-number)))

(defn handle-delete!
  [emp]
  (letfn [(alert-error!
            [msg]
            (js/alert msg))]
    (when (js/confirm (str "Delete employee " (.-id emp) "?"))
      (try-fetch-headers!
       (fn [h]
         (-> (js/fetch "/api" (js-obj "method" "DELETE"
                                      "body" (js->form emp)
                                      "headers" h))
             (.then #(.json %))
             (.then #(if (= % "OK")
                       (.reload js/location)
                       (alert-error! %)))
             (.catch alert-error!)))
       alert-error!))))

(defn render-employees!
  [emps]
  (letfn [(new-prop-cell
            [name value]
            (doto (new-elem "div" value)
              (.appendChild (new-hidden name value))))]
    (doseq [emp emps]
      (let [delete-btn (let [btn (new-elem "button" "Delete")]
                         (set! (.-type btn) "button")
                         (.addEventListener
                          btn
                          "click"
                          (fn [] (handle-delete! emp)))
                         btn)
            cells [(new-prop-cell "id" (.-id emp))
                   (new-prop-cell "name" (.-name emp))
                   (new-prop-cell "role" (.-role emp))
                   (new-prop-cell "salary" (.-salary emp))
                   (new-prop-cell "added-date" (.-added_date emp))
                   (doto (new-elem "div")
                     (.appendChild (new-elem "button" "Update")))
                   (doto (new-elem "div")
                     (.appendChild delete-btn))]
            row (let [form (new-elem "form")]
                  (set! (.-className form) "employee-row")
                  (set! (.-action form) "employee-form")
                  form)]
        (doseq [cell cells]
          (.appendChild row cell))
        (.appendChild (get-elem "table") row)))))

(defn render-error!
  [msg]
  (.appendChild (get-elem "table") (new-elem "h1" msg)))

(defn load-app!
  []
  (let [page-number (get-page-number)
        page-number-input (get-elem "page-number")]
    (set! (.-value page-number-input) page-number)
    (.addEventListener
     page-number-input
     "change"
     #(set! (.-href js/location) (str "?page=" (-> % .-target .-value))))
    (-> (js/fetch (str "/api?page=" page-number))
        (.then #(.json %))
        (.then #(if (instance? js/Object %)
                  (render-employees! (.-employees %))
                  (render-error! %)))
        (.catch render-error!))))

(load-app!)
