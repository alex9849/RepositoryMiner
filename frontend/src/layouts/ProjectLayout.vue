<template>
  <q-page
    padding
    class="q-col-gutter-lg"
  >
    <div>
      <q-tabs
        no-caps
        inline-label
        dense
        class="bg-grey-5 text-black shadow-2 rounded-borders"
        align="justify"
        switch-indicator
        active-bg-color="grey-2"
      >
        <q-route-tab
          :to="{name: 'ProjectBrowser'}"
          label="Browse"
          icon="code"
          append
          :ripple="false"
        />
        <q-route-tab
          :to="{name: 'ProjectSettings'}"
          label="Settings"
          icon="settings"
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
  name: "ProjectLayout"
}
</script>

<style scoped>

</style>
