
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
        path: 'project',
        component: () => import('pages/Projects')
      }, {
        name: 'addProject',
        path: 'project/add',
        component: () => import('pages/EditProject')
      }, {
        name: 'project',
        path: 'project/:id',
        component: () => import('layouts/ProjectLayout'),
        children: [
          {
            path: '',
            redirect: {name: 'ProjectBrowser'}
          }, {
            name: 'ProjectBrowser',
            path: 'browse',
            component: () => import('pages/ProjectBrowser')
          }, {
            name: 'ProjectSettings',
            path: 'settings',
            component: () => import('pages/ProjectSettings'),
            redirect: {name: 'ProjectSettingsAuthors'},
            children: [
              {
                name: 'ProjectSettingsAuthors',
                path: 'authors',
                component: () => import('pages/ProjectSettingsAuthors'),
              }
            ]
          }
        ]
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
