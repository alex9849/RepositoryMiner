<template>
  <q-page>
    <div
      class="row q-col-gutter-lg"
    >
      <div
        class="col-12 col-md-3"
      >
        <q-list
          bordered
          separator
          class="rounded-borders"
        >
          <q-item
            clickable
            v-ripple
            v-for="section of menuSections"
            :to="section.route"
            active-class="menuSelected"
          >
            <q-item-section>
              <q-item-label>
                {{ section.name }}
              </q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </div>

      <div
        class="col-12 col-md"
      >
        <div
          class="rounded-borders q-gutter-sm"
        >
          <router-view/>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "ProjectSettings",
  data() {
    return {
      menuSections: [
        {
          name: 'Authors',
          headline: 'Authors',
          route: {name: 'ProjectSettingsAuthors'}
        }
      ]
    }
  }
}
</script>

<style scoped>
  .menuSelected {
    border-left: 3px solid #f9836d;
  }
</style>
