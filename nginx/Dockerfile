# Stage 1: Build the React app
FROM node:16 AS build
WORKDIR /app
COPY hoopifront/*.json ./
RUN npm install && npm cache clean --force
COPY hoopifront ./
RUN npm run build && npm cache clean --force

# Stage 2: Serve the app with Nginx
FROM nginx:alpine
COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/default.conf /etc/nginx/conf.d
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80