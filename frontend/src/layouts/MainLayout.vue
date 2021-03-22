<template>
  <q-layout view="hHh LpR fFf">
    <q-header
      elevated
    >
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />

        <q-toolbar-title>
          RepoMiner
        </q-toolbar-title>

        <div>Quasar v{{ $q.version }}</div>
      </q-toolbar>
    </q-header>
    <q-drawer
      v-model="leftDrawerOpen"
      bordered
      show-if-above
      content-class="bg-primary text-white"
    >
      <q-list>
        <q-item
          v-for="sideBarLink in sideBarLinks"
          clickable
          :key="sideBarLink"
          :exact="sideBarLink.exact"
          :to="sideBarLink.to"
          active-class="q-item-no-link-highlighting"
        >
          <q-item-section
            avatar
          >
            <q-icon
              :name="sideBarLink.icon"
            />
          </q-item-section>
          <q-item-section>
            {{ sideBarLink.label }}
          </q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

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
import { openURL } from 'quasar';
import { mdiEarth} from "@quasar/extras/mdi-v5";
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
