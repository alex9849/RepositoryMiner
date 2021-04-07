<template>
  <div
    class="q-gutter-lg"
  >
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
            v-for="(pathBreadCrumb, index) in currentPathBreadCrumbs"
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
    <slot/>
    <q-card
      class="bg-grey-3"
    >
      <q-item-section
        v-if="loading"
        style="min-height: 100px"
      >
        <q-inner-loading showing>
          <q-spinner-pie size="50px" color="primary" />
        </q-inner-loading>
      </q-item-section>
      <q-card-actions
        vertical
        align="center"
        v-else-if="!Array.isArray(currentFiles)"
      >
        <q-icon
          size="100px"
          :color="!!currentFiles? '':'negative'"
          :name="!!currentFiles? 'description':'warning'"
        />
        <p v-if="!!currentFiles">{{ currentFiles.name }}</p>
        <p v-else>File doesn't exist!</p>
      </q-card-actions>

      <q-list
        separator
        v-else
      >
        <q-item
          v-for="item of currentFiles"
        >
          <q-item-section
            avatar
            style="min-width: 0"
          >
            <q-icon size="xs" color="primary" :name="item.folder? 'folder':'description'" />
          </q-item-section>
          <q-item-section>
            <q-btn
              flat
              dense
              no-caps
              style="max-width: max-content"
              @click="$emit('input', appendCurrentPath(item.name))"
            >
              {{ item.name }}
            </q-btn>
          </q-item-section>
          <q-item-section side>
            last commit/author?
          </q-item-section>
        </q-item>
        <q-item
          v-if="currentFiles.length === 0"
        >
          <q-item-section
            class="text-center"
          >
            <b>Folder is empty</b>
          </q-item-section>
        </q-item>
      </q-list>
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
  methods: {
    getFileTreeAtPath(pathParts, fileTree) {
      if(pathParts.length === 0) {
        return fileTree;
      }
      for(let file of fileTree) {
        if(file.name === pathParts[0]) {
          pathParts.splice(0, 1);
          if(file.folder) {
            return this.getFileTreeAtPath(pathParts, file.children)
          } else {
            return file
          }
        }
      }
      return null;
    },
    appendCurrentPath(folderOrFileName) {
      if(this.value === "") {
        return folderOrFileName;
      } else {
        return this.value + "/" + folderOrFileName;
      }
    }
  },
  computed: {
    currentPathBreadCrumbs() {
      let pathParts = this.value.split("/");
      let breadCrumbData = [];
      if(pathParts.length === 0
        || (pathParts.length === 1 && !pathParts[0])) {
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
      return breadCrumbData;
    },
    currentFiles() {
      let pathParts = this.value.split("/");
      if(pathParts.length === 0
        || (pathParts.length === 1 && !pathParts[0])) {
        return this.fileTree;
      }
      for(let file of this.fileTree) {
        if(file.name === pathParts[0]) {
          pathParts.splice(0, 1);
          if(file.folder) {
            return this.getFileTreeAtPath(pathParts, file.children)
          } else {
            return file
          }
        }
      }
      return null;
    }
  }
}
</script>

<style scoped>

</style>
