ostack-swift-analytics
======================

Analytics in Cascalog for Openstack Swift Access Logs

Use https://github.com/nbhavana/swift-log-generator to generate an access log,
that will simulate openstack's swift access logs.

Place the output of the above in some dir and refer it in below sample runs.


Query to count how many response codes were present for all content-providers that match '403'
; (?- (stdout) (countrespcodes "403" ))

Query to count response codes for certain content-provider that match certain value.
; (?- (stdout) (countrespcodes-for-cp "200" "AUTH_APPLE")) 

Query to transform the input access log format into a custom log 
; (?- (stdout) (transformlog "AUTH_APPLE" ))

Query to transform the input access log format into a custom log with a custom delimiter
; (?- (hfs-delimited "/home/knawale/outputs/out1" :delimiter ";") (transformlog "AUTH_APPLE" ))

Query with new delimiter and the output is in compressed format.
; (?- (hfs-textline "/home/knawale/outputs/out2" :delimiter ";" :compression :enable) (transformlog "AUTH_APPLE" ))
