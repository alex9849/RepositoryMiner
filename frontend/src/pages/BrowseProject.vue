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
      <div
        class="row justify-center q-gutter-x-lg"
      >
        <q-inner-loading
          showing
          v-if="chartOptions.loading"
        >
          <q-spinner-pie size="50px"/>
        </q-inner-loading>
        <q-btn
          v-else
          v-for="chart of requestableChartsByPath"
          size="md"
          class="bg-amber"
          no-caps
          rounded
          :label="chart.name"
          :icon="chart.icon"
        />
      </div>
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
import PackedBubbleService from "src/service/chartServices/PackedBubbleService";

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
        chartOptions: {}
      },
      chartOptions: {
        requestableCharts: [],
        loading: true
      }
    }
  },
  created() {
    if(!!this.$route.query.path) {
      this.browser.currentPath = this.$route.query.path;
    } else {
      this.browser.currentPath = "";
    }
    this.chartDialog.chartOptions = PackedBubbleService.exampleBackendData;
    ProjectService.getProjectFileTree(this.projectId)
      .then(fileTree => this.browser.fileTree = fileTree)
      .finally(() => this.browser.loading = false);
    ProjectService.getRequestableProjectCharts(this.projectId, "FILE_BROWSER")
      .then(charts => this.chartOptions.requestableCharts = charts)
      .finally(() => this.chartOptions.loading = false);
  },
  computed: {
    projectId() {
      return this.$route.params.id;
    },
    requestableChartsByPath() {
      //return this.chartOptions.requestableCharts;
      let returnMe = [];
      returnMe.push({
        name: "File and folder chart",
        icon: "folder",
        availableContext: [{
          viewContext: "FILE_BROWSER",
          subContext: ["FILE", "FOLDER"]
        }]
      });
      returnMe.push({
        name: "Folder-chart",
        icon: "folder",
        availableContext: [{
          viewContext: "FILE_BROWSER",
          subContext: "FOLDER"
        }]
      });
      returnMe.push({
        name: "File-chart",
        icon: "folder",
        availableContext: [{
          viewContext: "FILE_BROWSER",
          subContext: "FILE"
        }]
      });
      return returnMe;
    }
  }
}
</script>

<style scoped>

</style>
