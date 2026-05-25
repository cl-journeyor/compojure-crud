(ns misc.core)

(defn try-convert-vals
  "Takes a source map and a second map with the desired keys and converter
  functions to be used against the values of the first. Returns a map with keys
  of `cvters-m` and converted values of `src-m`, or nil if any of the
  converters throw an exception due to a conversion error."
  [src-m cvters-m]
  (try
    (reduce
     (fn [map [k v]]
       (assoc map k (v (get src-m k))))
     {}
     cvters-m)
    (catch Exception _ nil)))
