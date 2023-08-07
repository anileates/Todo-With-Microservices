FROM node:18-alpine

# Create app directory
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .

EXPOSE 8085
CMD [ "node", "server.js" ]