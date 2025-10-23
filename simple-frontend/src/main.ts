import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import Home from './pages/Home.vue'
import TravelChat from './pages/TravelChat.vue'
import TravelAgent from './pages/TravelAgent.vue'
import Tools from './pages/Tools.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/travel', name: 'travel', component: TravelChat },
    { path: '/agent', name: 'agent', component: TravelAgent },
    { path: '/tools', name: 'tools', component: Tools }
  ]
})

createApp(App).use(router).mount('#app')

