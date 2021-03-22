<template>
  <q-page
    padding
    class="q-gutter-lg"
  >
    <h4
      class="row justify-center"
    >
      Projects
    </h4>
    <div
      class="row justify-center"
    >
      <q-btn
        color="positive"
      >
        Add Project
      </q-btn>
    </div>
    <div class="row justify-center">
      <q-list
        bordered
        class="rounded-borders full-width"
        separator
        style="max-width: 800px"
      >
        <q-item
          v-if="loading"
        >
          <q-item-section>
            <div>
              <q-inner-loading
                :showing="true"
              >
                <div>
                  <q-spinner-pie
                    color="primary"
                    size="2.5em"
                    style="margin-right: 10px"
                  />
                  Loading...
                </div>
              </q-inner-loading>
            </div>
          </q-item-section>
        </q-item>
        <q-item
          v-if="!loading && projects.length === 0"
        >
          <q-item-section
            class="text-center"
          >
            No Projects have been added yet!
          </q-item-section>
        </q-item>
        <q-item
          v-for="project in projects"
          :key="project"
        >
          <q-item-section avatar top>
            <q-avatar icon="folder" color="primary" text-color="white"/>
          </q-item-section>
          <q-item-section>
            <q-item-label lines="1">
              {{ project.name }}
            </q-item-label>
            <q-item-label caption>
              Last edited {{ project.lastUpdate }}
            </q-item-label>
          </q-item-section>
          <q-item-section side>
            <div class="q-gutter-sm">
              <q-btn
                icon="search"
                color="info"
                label="Select"
              />
              <q-btn
                icon="delete"
                color="negative"
              />
            </div>
          </q-item-section>
        </q-item>
      </q-list>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "Projects",
  data: () => {
    return {
      loading: true,
      projects: []
    }
  },
  created() {
    this.loadProjects();
  },
  methods: {
    loadProjects() {
      this.loading = true;
      this.projects = [];
      setTimeout(() => {
        this.loading = false;
        this.projects = [
          {
            id: 1,
            name: "TestProject",
            lastUpdate: new Date()
          }
        ]
      }, 2000)
    }
  }
}
</script>

<style scoped>

</style>
