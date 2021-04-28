<template>
  <q-dialog
    full-width
    :value="value"
    @input="$emit('input', $event)"
  >
    <q-card
      class="bg-white q-ma-lg"
    >
      <q-bar
        class="bg-black text-white"
      >
        <div
          class="col text-center text-weight-bold"
        >
          {{ loading? 'Loading...' : name }}
        </div>
        <q-btn
          dense
          flat
          icon="close"
          class="text-white"
          @click="$emit('input', false)"
        >
          <q-tooltip
            content-class="bg-gray-5 text-white"
          >
            Close
          </q-tooltip>
        </q-btn>
      </q-bar>
      <q-card-section v-if="!!description && !loading">
        <div class="text-h6">Description:</div>
        {{ description }}
      </q-card-section>
      <q-separator/>
      <q-card-section
        v-if="loading"
        style="height: 200px"
      >
        <q-inner-loading
          showing
        >
          <q-spinner-pie size="50px"/>
        </q-inner-loading>
      </q-card-section>
      <q-card-section
        v-if="!loading"
        class="row justify-center items-center"
      >
        <highcharts :options="hcChartOptions" :highcharts="hcInstance" style="width: 100%;"/>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import Highcharts from 'highcharts'

require('highcharts/highcharts-more.js')(Highcharts);
require('highcharts/modules/drilldown.js')(Highcharts);

export default {
  name: "ChartDialog",
  props: {
    value: {
      type: Boolean,
      required: true
    },
    loading: {
      type: Boolean,
      default: false
    },
    description: {
      type: String
    },
    name: {
      type: String,
      required: true
    },
    hcChartOptions: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      hcInstance: Highcharts
    }
  },
  created() {
    document.addEventListener("resize", this.onResize)
  },
  destroyed() {
    document.removeEventListener("resize", this.onResize)
  },
  methods: {
    onResize(event) {
      this.hcInstance.charts[0].reflow();
    }
  }
}
</script>

<style scoped>

</style>
