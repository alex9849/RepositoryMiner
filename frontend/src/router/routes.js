
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      {
        name: 'root',
        path: '',
        redirect : {name: 'projects'},
      }, {
        name: 'projects',
        path: '/projects',
        component: () => import('pages/Projects')
      }
    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '*',
    component: () => import('pages/Error404.vue')
  }
]

export default routes
