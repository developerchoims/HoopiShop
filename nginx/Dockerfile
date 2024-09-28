# Stage 1: Build the React app
FROM node:18 AS build
WORKDIR /app
ENV NODE_OPTIONS=--openssl-legacy-provider
COPY hoopifront/*.json ./
RUN npm install
COPY hoopifront ./
RUN npm run build

# Stage 2: Serve the app with Nginx
FROM nginx:alpine
COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/default.conf /etc/nginx/conf.d
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
