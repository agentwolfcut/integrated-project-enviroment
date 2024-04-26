FROM node:22.0.0
# FROM node: 20.11.1
WORKDIR : /usr/src/app

COPY ./package.json ./

RUN npm install

COPY ../main.js ./

EXPOSE  5000

CMD [ "node", "./main.js" ]