# Parcel Management Angular (src only)

This folder contains **only** the Angular `src/` code so you can copy it into a fresh Angular project.

## How to use
1) Create a new Angular project (module-based):
```bash
ng new parcel-management-frontend --routing --style=css
# When prompted: "Use standalone components?" -> choose "No"
cd parcel-management-frontend
```

2) Install Bootstrap (optional but recommended):
```bash
npm i bootstrap
```
Add `node_modules/bootstrap/dist/css/bootstrap.min.css` to `styles` in `angular.json`.

3) Copy the content of this folder's `src/` into your Angular project's `src/` (replace existing files).

4) Run:
```bash
ng serve
```
App runs at `http://localhost:4200`.

## Notes
- Backend API base URL is in `src/environments/environment.ts`
- Officer demo login: `officer@parcel.com` / `officer123`
