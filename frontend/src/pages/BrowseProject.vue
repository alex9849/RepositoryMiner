<template>
  <q-page
    padding
  >
    <file-browser
      v-model="browser.currentPath"
      :file-tree="browser.fileTree"
      :loading="browser.loading"
      @input="$router.push({name: 'browseProject', params: {id: $route.params.id}, query: {path: $event? $event: undefined}})"
    >
      <chart-dialog
        v-model="chartDialog.show"
        :loading="chartDialog.loading"
        :chart-options="chartDialog.chartOptions"
      />
    </file-browser>
  </q-page>
</template>

<script>

import FileBrowser from "components/FileBrowser";
import ProjectService from "src/service/ProjectService";
import ChartDialog from "components/ChartDialog";

export default {
  name: "BrowseProject",
  components: {ChartDialog, FileBrowser},
  data: () => {
    return {
      browser: {
        fileTree: [],
        currentPath: "",
        loading: true
      },
      chartDialog: {
        show: true,
        loading: false,
        chartOptions: {
          name: 'TestGraph',
          description: 'Test description',
          type: 'packedbubble',
          series: [{
            name: 'Europa',
            data: [{
              name: 'DE',
              value: 20,
              drilldown: {
                name: 'DE',
                data: [{
                  name: 'RLP',
                  value: 5
                }, {
                  name: 'BW',
                  value: 10
                }]
              }
            }, {
              name: 'FR',
              value: 25
            }, {
              name: 'ES',
              value: 15
            }]
          }, {
            name: 'Europa2',
            data: [{
              name: 'DE',
              value: 20,
              drilldown: {
                name: 'DEee',
                data: [{
                  name: 'RLP',
                  value: 5
                }, {
                  name: 'BW',
                  value: 10
                }]
              }
            }, {
              name: 'FR',
              value: 25
            }, {
              name: 'ES',
              value: 15
            }]
          }]
        }
      }
    }
  },
  created() {
    if(!!this.$route.query.path) {
      this.browser.currentPath = this.$route.query.path;
    } else {
      this.browser.currentPath = "";
    }
    ProjectService.getProjectFileTree(this.projectId)
      .then(fileTree => this.browser.fileTree = fileTree)
      .finally(() => this.browser.loading = false);
  },
  computed: {
    projectId() {
      return this.$route.params.id;
    }
  }
}
</script>

<style scoped>

</style>
