import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5997,
    host: '0.0.0.0', // Needed for Docker to expose the port correctly
    strictPort: true
  }
})
