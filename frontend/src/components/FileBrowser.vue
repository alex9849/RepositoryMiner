<template>
  <div>
    <q-card
      bordered
    >
      <q-toolbar
        class="bg-grey-3"

      >
        <q-breadcrumbs
          gutter="xs"
        >
          <q-breadcrumbs-el>
            <q-btn
              icon="home"
              flat
              dense
              no-caps
              @click="$emit('input', '')"
            />
          </q-breadcrumbs-el>
          <q-breadcrumbs-el
            v-for="(pathBreadCrumb, index) in pathBreadCrumbs"
          >
            <q-btn
              :label="pathBreadCrumb.label"
              flat
              dense
              no-caps
              @click="$emit('input', pathBreadCrumb.path)"
            />
          </q-breadcrumbs-el>
        </q-breadcrumbs>
      </q-toolbar>
    </q-card>
  </div>
</template>

<script>
export default {
  name: "FileBrowser",
  props: {
    fileTree: {
      type: Array,
      default: () => []
    },
    value: {
      type: String,
      required: true
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    pathBreadCrumbs() {
      let pathParts = this.value.split("/");
      let breadCrumbData = [];
      if(pathParts.length === 1 && !pathParts[0]) {
        return breadCrumbData;
      }
      let fullPath = "";
      let isFirst = true;
      for(let path of pathParts) {
        if(!isFirst) {
          fullPath += "/";
        } else {
          isFirst = false;
        }
        fullPath += path
        breadCrumbData.push({
          label: path,
          path: fullPath
        })
      }
      if(breadCrumbData.length > 0 && !breadCrumbData[pathParts.length - 1].label) {
        return breadCrumbData.splice(breadCrumbData.length - 1, 1)
      }
      return breadCrumbData;
    }
  }
}
</script>

<style scoped>

</style>
