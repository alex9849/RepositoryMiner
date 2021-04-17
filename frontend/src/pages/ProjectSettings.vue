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
          class="bg-amber-1 rounded-borders"
        >
          Content
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
          name: 'authors',
          route: {name: ''}
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>
