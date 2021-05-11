<template>
  <q-layout view="hHh LpR fFf">
    <q-header
      elevated
    >
      <q-toolbar>
        <q-btn
          flat
          dense
          icon="home"
          no-caps
          :ripple="false"
          :to="{name: 'projects'}"
        >
          <q-toolbar-title>
            RepoMiner
          </q-toolbar-title>
        </q-btn>
      </q-toolbar>
    </q-header>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<style scoped>
a {
  text-decoration: none;
  color: inherit;
}
</style>

<script>
import {openURL} from 'quasar';
import {mdiEarth} from "@quasar/extras/mdi-v5";

export default {
  name: 'MainLayout',
  data() {
    return {
      desktopModeBreakPoint: 1023,
      leftDrawerOpen: false,
      windowWidth: 0,
      sideBarLinks: [
        {
          label: 'Projects',
          icon: mdiEarth,
          to: {
            name: 'projects'
          },
          exact: false
        }
      ]
    }
  },
  created() {
  },
  methods: {
    openURLInBrowser(url) {
      openURL(url)
    }
  }
}
</script>
