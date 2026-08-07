FROM node:20.15.0-alpine

WORKDIR /app/api

COPY package.json ./

RUN npm install -g npm@latest
RUN if [ "$NODE_ENV" = "dev" ]; then npm i; else npm i --omit=dev; fi

COPY . .

EXPOSE 3000 3001
CMD ["npm", "start"]
