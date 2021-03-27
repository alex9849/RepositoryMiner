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
        @click="$router.push({ name: 'addProject' })"
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
          :key="project.id"
        >
          <q-item-section avatar top>
            <q-avatar icon="folder" color="primary" text-color="white"/>
          </q-item-section>
          <q-item-section>
            <q-item-label lines="1">
              {{ project.name }}
            </q-item-label>
            <q-item-label caption>
              Created on {{ project.lastUpdate.toLocaleDateString() }} at {{ project.lastUpdate.toLocaleTimeString() }}
            </q-item-label>
          </q-item-section>
          <q-item-section side>
            <div class="q-gutter-sm">
              <q-btn
                icon="search"
                color="positive"
                label="Select"
              />
              <q-btn
                icon="delete"
                color="negative"
                @click="() => {deleteDialog.project = project; deleteDialog.display = true}"
              />
            </div>
          </q-item-section>
        </q-item>
      </q-list>
      <c-question
        :question="'Delete Project \'' + deleteDialog.project.name + '\'?'"
        v-model="deleteDialog.display"
        ok-button-text="Delete"
        ok-color="negative"
        :loading="deleteDialog.loading"
        @clickOk="onDeleteProject"
        @clickAbort="() => {deleteDialog.display = false}"
      />
    </div>
  </q-page>
</template>

<script>
import ProjectService from "src/service/ProjectService";
import CQuestion from "components/CQuestion";

export default {
  name: "Projects",
  components: {CQuestion},
  data: () => {
    return {
      loading: true,
      projects: [],
      deleteDialog: {
        project: '',
        display: false,
        loading: false
      }
    }
  },
  created() {
    this.loadProjects();
  },
  methods: {
    loadProjects() {
      this.loading = true;
      this.projects = [];
      this.$q.loadingBar.start()
      ProjectService.getProjects()
        .then(projects => {
          this.projects = projects;
        })
        .finally(() => {
          this.loading = false;
          this.$q.loadingBar.stop();
        });
    },
    onDeleteProject() {
      this.deleteDialog.loading = true;
      ProjectService.deleteProject(this.deleteDialog.project.id)
        .then(() => {
          this.deleteDialog.project = '';
          this.deleteDialog.display = false;
          this.loadProjects();
        }, (error) => {
          this.$q.notify({
            type: 'negative',
            message: 'Couldn\'t delete project: ' + error.response.data.message
          });
      }).finally(() => {
        this.deleteDialog.loading = false;
      })
    }
  }
}
</script>

<style scoped>

</style>
