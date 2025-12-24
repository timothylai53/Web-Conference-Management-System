# --- Stage 1: Build Environment ---
# Use a lightweight Node image for building the application
FROM node:20-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json first to leverage Docker's layer caching
COPY package*.json ./

# Install project dependencies
RUN npm install

# Copy the rest of the frontend source code
COPY . .

# Execute the build command (generates the 'dist' folder)
RUN npm run build -- --configuration production

# --- Stage 2: Runtime Environment (Nginx) ---
# Use a lightweight Nginx image to serve the static assets
FROM nginx:alpine

# Copy the compiled build artifacts from the build stage to Nginx's default HTML directory
# ⚠️ Note: For Angular 17+, the default path is usually 'dist/frontend/browser'.
# If your project name is different, please update the path according to your actual 'dist' subfolder name.
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# Expose port 80 (standard HTTP port)
EXPOSE 80

# Start Nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]
