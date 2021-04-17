<template>
  <q-page
    padding
    class="q-col-gutter-lg"
  >
    <div>
      <q-tabs
        no-caps
        inline-label
        indicator-color="red"
        class="bg-grey-3 text-black shadow-2 rounded-borders"
        align="justify"
        switch-indicator
      >
        <q-route-tab
          v-for="tab in tabs"
          :to="tab.to"
          :label="tab.label"
          :icon="tab.icon"
          append
          :ripple="false"
        />
      </q-tabs>
    </div>
    <div>
      <router-view/>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "ProjectLayout",
  data() {
    return {
      tabs: [{
        label: 'Browse',
        icon: 'code',
        to: {name: 'ProjectBrowser'}
      }, {
        label: 'Settings',
        icon: 'settings',
        to: {name: 'ProjectSettingsAuthors'}
      }]
    }
  }
}
</script>

<style scoped>

</style>
