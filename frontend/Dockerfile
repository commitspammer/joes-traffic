FROM node:16

ENV PORT=3000


WORKDIR /app

COPY . .

RUN npm install yarn
RUN yarn install

EXPOSE 3000

RUN yarn build
CMD [ "yarn", "start" ]