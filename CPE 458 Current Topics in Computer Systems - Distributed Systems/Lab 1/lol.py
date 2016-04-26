for x in xrange(1,24):
    if x < 10:
        print "ssh" + " 127x0" + str(x) + " &"
    else:
        print "ssh" + " 127x" + str(x) + " &"
    print "yes"