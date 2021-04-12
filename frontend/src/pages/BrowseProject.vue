<template>
  <q-page
    padding
  >
    <file-browser
      v-model="browser.currentPath"
      :file-tree="browser.fileTree"
      :loading="browser.loading"
      @isCurrentFileAFile="browser.isFile = $event"
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
          @click="requestChart(chart.identifier)"
        />
      </div>
      <chart-dialog
        v-model="chartDialog.show"
        :loading="chartDialog.loading"
        :name="chartDialog.chartOptions.name"
        :description="chartDialog.chartOptions.description"
        :hc-chart-options="chartDialog.chartOptions.graphConfig"
      />
    </file-browser>
  </q-page>
</template>

<script>

import FileBrowser from "components/FileBrowser";
import ProjectService from "src/service/ProjectService";
import ChartDialog from "components/ChartDialog";
import ChartService from "src/service/ChartService";

export default {
  name: "BrowseProject",
  components: {ChartDialog, FileBrowser},
  data: () => {
    return {
      browser: {
        fileTree: [],
        currentPath: "",
        loading: true,
        isFile: false
      },
      chartDialog: {
        show: false,
        loading: true,
        chartOptions: {
          name: '',
          description: '',
          graphConfig: {}
        }
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
    ProjectService.getProjectFileTree(this.projectId)
      .then(fileTree => this.browser.fileTree = fileTree)
      .finally(() => this.browser.loading = false);
    ProjectService.getRequestableProjectCharts(this.projectId, "FILE_BROWSER")
      .then(charts => this.chartOptions.requestableCharts = charts)
      .finally(() => this.chartOptions.loading = false);
  },
  methods: {
    requestChart(identifier) {
      this.chartDialog.loading = true;
      this.chartDialog.show = true;
      ProjectService.getChart(this.projectId, identifier, {path: this.currentPath})
        .then(chartData => {
          this.chartDialog.chartOptions = ChartService.parseBackendToOptions(chartData);
          this.chartDialog.loading = false;
        });
    }
  },
  computed: {
    projectId() {
      return this.$route.params.id;
    },
    requestableChartsByPath() {
      let availableSubContext = new Set;
      if(this.browser.isFile) {
        availableSubContext.add("FILE")
      } else {
        availableSubContext.add("FOLDER")
      }
      //return this.chartOptions.requestableCharts;
      let returnMe = [];
      for(let graph of this.chartOptions.requestableCharts) {
        if(graph.availableContext.some(ctx => ctx.subContext.some( x => availableSubContext.has(x)))) {
          returnMe.push(graph);
        }
      }
      return returnMe;
    }
  }
}
</script>

<style scoped>

</style>
