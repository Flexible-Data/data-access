FROM java:7
MAINTAINER tanjie "584135970@qq.com"

ADD data-access.tar.gz /
RUN mv /data-access /opt/data-access
ENTRYPOINT /opt/data-access/bin/start.sh $PORT && tail -f /opt/data-access/logs/stdout.log