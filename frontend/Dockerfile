# develop stage
FROM node:latest as develop-stage
WORKDIR /app

# Copy the package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy rest of the files
COPY . .

# Build the project
RUN npm run build

FROM nginx:alpine as production-build

# Copy from the stahg 1
COPY --from=develop-stage /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/nginx.conf

ENTRYPOINT ["nginx", "-g", "daemon off;"]