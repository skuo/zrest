from __future__ import with_statement
from fabric.api import cd, env, lcd, local, prefix, put, run, settings, sudo, task
from fabric.network import ssh

import fnmatch
import os
import time

VERSION="0.1.0-SNAPSHOT-local"

projName="zrest"
tarGzFile=""
ver=""

# fab build_and_debug:0.1.0-SNAPSHOT-local
# fab build_and_debug:version=0.1.0-SNAPSHOT-local,password=password
@task
def build_and_debug(version=VERSION):
    with settings(warn_only=True):
        local("pwd")
        local("./target/zrest-%s/bin/stopZRest.sh" % version)
        local("mvn clean install -P local")
        local("cd target; tar -zxvf zrest-%s.tar.gz" % version)
        #replace_properties()
        #
        local("cd target/zrest-%s; ./bin/debugZRest.sh" % version)
        print "\nwait 3 seconds before tailing\n"
        time.sleep(3)
        local("tail -100f target/zrest-%s/logs/*.stderrout.log" % version)
   
@task
def build_skip_tests_and_debug(version=VERSION):
    with settings(warn_only=True):
        local("pwd")
        local("./target/zrest-%s/bin/stopRoyaltyWeb.sh" % version)
        local("mvn clean install -DskipTests -P local")
        local("cd target; tar -zxvf zrest-%s.tar.gz" % version)
        #replace_properties()
        #
        local("cd target/zrest-%s; ./bin/debugZRest.sh" % version)
        print "\nwait 3 seconds before tailing\n"
        time.sleep(3)
        local("tail -100f target/zrest-%s/logs/*.stderrout.log" % version)

@task
def build_and_start(version=VERSION):
    with settings(warn_only=True):
        local("pwd")
        local("./target/zrest-%s/bin/stopZRest.sh" % version)
        local("mvn clean install -P local")
        local("cd target; tar -zxvf zrest-%s.tar.gz" % version)
        replace_properties()
        #
        local("cd target/zrest-%s; ./bin/startZRest.sh" % version)
        print "\nwait 3 seconds before tailing\n"
        time.sleep(3)
        local("tail -100f target/zrest-%s/logs/*.stderrout.log" % version)

@task
def restart_and_debug(version=VERSION):
    with settings(warn_only=True):
        local("pwd")
        local("./target/zrest-%s/bin/stopZRest.sh" % version)
        local("cd target/zrest-%s; ./bin/debugZRest.sh" % version)
        print "\nwait 3 seconds before tailing\n"
        time.sleep(3)
        local("tail -100f target/royalty-web-%s/logs/*.stderrout.log" % version)

def replace_properties(version=VERSION):
    global PASSWORD
    global CLIENT_SECRET
    global API_KEYS
    global MAIL_SERVER_PASSWORD
    global DB_PASSWORD
    global FI022_PASSWORD
    global SFDC_OAUTH2_CLIENT_SECRET
    global SFDC_OAUTH2_PASSWORD
    global PAYPAL_OAUTH2_CLIENT_SECRET
    # set password or secret from environment variables in file with sed
    PASSWORD = os.environ.get('INT_FI001_SFTP_PASSWORD') 
    CLIENT_SECRET = os.environ.get('DEV_OAUTH2_CLIENT_SECRET') 
    API_KEYS = os.environ.get('DEV_API_KEYS') 
    MAIL_SERVER_PASSWORD = os.environ.get('MAIL_SERVER_PASSWORD') 
    DB_PASSWORD = os.environ.get('DB_PASSWORD')
    FI022_PASSWORD = os.environ.get('INT_FI022_SFTP_PASSWORD')
    SFDC_OAUTH2_CLIENT_SECRET = os.environ.get('INT_SFDC_OAUTH2_CLIENT_SECRET')
    SFDC_OAUTH2_PASSWORD = os.environ.get('INT_SFDC_OAUTH2_PASSWORD')
    PAYPAL_OAUTH2_CLIENT_SECRET = os.environ.get('INT_PAYPAL_OAUTH2_CLIENT_SECRET')
    sedCmd = "sed -i.bak 's/dummyPassword/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (PASSWORD,version)
    local(sedCmd)
    sedCmd = "sed -i.bak 's/dummyClientSecret/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (CLIENT_SECRET,version)
    local(sedCmd)
    sedCmd = "sed -i.bak 's/dummyApiKeys/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (API_KEYS,version)
    local(sedCmd)
    sedCmd = "sed -i.bak 's/dummyMailServerPassword/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (MAIL_SERVER_PASSWORD,version)
    local(sedCmd)
    sedCmd = "sed -i.bak 's/dummyDbPassword/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (DB_PASSWORD,version)
    local(sedCmd)
    sedCmd = "sed -i.bak 's/dummyFI022Password/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (FI022_PASSWORD,version)
    local(sedCmd)
    sedCmd = "sed -i.bak 's/sdfcOauth2ClientSecret/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (SFDC_OAUTH2_CLIENT_SECRET,version)
    local(sedCmd)
    sedCmd = "sed -i.bak 's/sfdcOauth2Password/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (SFDC_OAUTH2_PASSWORD,version)
    local(sedCmd)
    sedCmd = "sed -i.bak 's/paypalOauth2ClientSecret/%s/g' target/royalty-web-%s/conf/royalty-web-override.properties" \
        % (PAYPAL_OAUTH2_CLIENT_SECRET,version)
    local(sedCmd)

#-----------------------------------------------------------------
# find files by pattern
def find(pattern, path):
    result = []
    for root, dirs, files in os.walk(path):
        for name in files:
            if fnmatch.fnmatch(name, pattern):
                #result.append(os.path.join(root, name))
                # only interested in name
                result.append(name)
    return result

def findTarGzFile():
    global tarGzFile
    global ver
    files = find("%s-*.tar.gz" % projName, "target")
    if (len(files) == 0):
        raise TypeError("%s-*.tar.gz not found" % projName)
    tarGzFile = files[0]
    print ".tar.gz file = %s" % tarGzFile
    ver = tarGzFile[:-len(".tar.gz")]
    print "ver = %s" % ver

@task
def host_type():
    run('uname -a')
