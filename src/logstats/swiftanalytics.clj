(ns logstats.swiftanalytics
    (:use [cascalog.api :as capi])
    (:use [cascalog.more-taps :as ctaps])
    (:use [clojure.string :only [split]])
    (:use [cascalog.logic.ops :as ops])
)


(def sample-file "/home/knawale/swift-10.log")
;(def sample-file "/home/knawale/swift-1-MM.log")

(def dest
     (hfs-textline "/tmp/anat.out")
     )

(def src
     (hfs-textline sample-file
   		    :outfields ["?line" ]
		  ))


(def outfields 
     ["?f1" "?f2" "?f3" "?f4" "?f5" "?f6" "?f7" "?f8" "?f9" "?f10" "?f11" "?f12" "?f13" "?f14" "?f15" "?f16" "?f17" "?f18" "?f19" "?f20" "?f21" "?f22"])

(defn get3dec [val]
       (subs (str val) 0 (+ (.indexOf (str val) ".") 4)))

(defmapfn long [value]
	  (Long/parseLong value))
	  
(defmapfn divbynum [num divisor]
	  (div (long num) divisor))


; Query for counting response codes

(defn countrespcodes 
      [respcode] 
      (<- [?count] 
      (src ?line)
      (split ?line #"\s+" :>> outfields)
      (= respcode ?f12)
      (count ?count)
       ))

; Query for counting response codes for certain content provider
(defn countrespcodes-for-cp
      [respcode cp] 
      (<- [?count] 
      (src ?line)
      (split ?line #"\s+" :>> outfields)
      (= respcode ?f12)
      (split ?f10 #"/"  4 :> _ _ ?cp _)
      (= ?cp cp)
      (count ?count)
       ))

; query to create new log format,
; create log entry only if a field matches passed in parameter,
; produce fields out of sequence
; divide a input field by a number,
; create a new field

(defn transformlog 
      [cp] 
      (<- [?f12 ?f9 ?f16 ?f21 ?f34] 
       (src ?line)
       (split ?line #"\s+" :>> outfields)
       (split ?f10 #"/"  4 :> _ _ ?cp _)
       (= ?cp cp)
	(divbynum ?f16  1000 :> ?f34)
       ))


; (?- (stdout) (countrespcodes "403" ))
; (?- (stdout) (countrespcodes-for-cp "200" "AUTH_APPLE")) 
; (?- (stdout) (transformlog "AUTH_APPLE" ))  ; SAMSUNG, change 1024 - 1000

; query with new delimiter
; (?- (hfs-delimited "/home/knawale/outputs/out1" :delimiter ";") (transformlog "AUTH_APPLE" ))

; query with new delimiter and compression
; (?- (hfs-textline "/home/knawale/outputs/out2" :delimiter ";" :compression :enable) (transformlog "AUTH_APPLE" ))
