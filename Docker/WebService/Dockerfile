FROM almalinux:8-minimal as intermediate
LABEL stage=intermediate
RUN microdnf install -y git
RUN git clone --depth 1 --branch master https://github.com/racai-ai/third_person.git

FROM almalinux:8-minimal
ENV container=docker

# System stuff
RUN microdnf update && microdnf install -y tar zip unzip curl wget mc glibc-langpack-ro glibc-langpack-en python3.11 java-17-openjdk-headless httpd php php-mbstring php-json php-intl php-cli php-xml psmisc gcc gcc-c++ make automake autoconf flex bison logrotate
# python38 python38-devel 
ENV LC_ALL=en_US.UTF-8
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en

# Python venv
#RUN python3.8 -m venv /venv-3.8
RUN python3.11 -m venv /venv-3.11

# Modules
COPY --from=intermediate /third_person/WebServiceModules/ /modules/
RUN rm -f /bin/sh ; ln -s /bin/bash /bin/sh
RUN source /venv-3.11/bin/activate && pip3 install --upgrade pip &&  pip3 install -r /modules/TextExtractor/requirements.txt && pip3 install torch --index-url https://download.pytorch.org/whl/cpu && pip3 install rwpt scikit-learn

# RNER
#RUN source /venv-3.8/bin/activate && pip3 install --upgrade pip &&  pip3 install -r /modules/RNER/requirements.txt && pip3 install torch==1.7.1+cpu --index-url https://download.pytorch.org/whl/cpu && pip3 install rwpt scikit-learn spacy flask
COPY --from=intermediate /third_person/Rephrase/ /Rephrase/

# Setup site
COPY --from=intermediate /third_person/WebService/web/ /site/
RUN chown -R apache:apache /site/
COPY conf/httpd.conf /etc/httpd/conf/httpd.conf
COPY conf/php.ini /etc/
ENV APACHE_RUN_USER apache
ENV APACHE_RUN_GROUP apache
ENV APACHE_LOG_DIR /var/log/httpd
ENV APACHE_LOCK_DIR /var/lock/httpd
ENV APACHE_PID_FILE /var/run/httpd.pid


COPY docker/entrypoint.sh /
RUN chmod a+rx /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
CMD ["/entrypoint.sh"]
