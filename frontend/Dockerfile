FROM nginx

# 映射 HTML
COPY ./dist ./usr/share/nginx/html
# Conf

COPY dist/conf/nginx.conf ./etc/nginx/conf.conf
COPY dist/conf/conf.d ./etc/nginx/conf.d

EXPOSE 8090

