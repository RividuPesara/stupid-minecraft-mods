set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
ID="${1:-}"

if [[ -z "$ID" ]]; then
	echo "usage: $0 <mod-id>" >&2
	exit 1
fi

if [[ ! "$ID" =~ ^[a-z][a-z0-9_-]{1,63}$ ]]; then
	echo "invalid mod id '$ID': use lowercase letters, digits, hyphens or underscores, starting with a letter" >&2
	exit 1
fi

if [[ -e "$ROOT/$ID" ]]; then
	echo "'$ID' already exists" >&2
	exit 1
fi

PKG="$(echo "$ID" | tr -d '_-')"
CLASS="$(echo "$ID" | awk -F'[-_]' '{for (i = 1; i <= NF; i++) printf "%s", toupper(substr($i, 1, 1)) substr($i, 2)}')"

cp -r "$ROOT/template" "$ROOT/$ID"
rm -rf "$ROOT/$ID/build" "$ROOT/$ID/.gradle" "$ROOT/$ID/run" "$ROOT/$ID/bin"

mv "$ROOT/$ID/src/main/java/com/example/templatemod" "$ROOT/$ID/src/main/java/com/example/$PKG"
mv "$ROOT/$ID/src/main/java/com/example/$PKG/TemplateMod.java" "$ROOT/$ID/src/main/java/com/example/$PKG/$CLASS.java"
mv "$ROOT/$ID/src/main/resources/assets/template-mod" "$ROOT/$ID/src/main/resources/assets/$ID"

FILES=$(grep -rl -E 'template|Template' "$ROOT/$ID" --exclude-dir=build --exclude-dir=.gradle || true)
for f in $FILES; do
	sed -i \
		-e "s/com\.example\.templatemod/com.example.$PKG/g" \
		-e "s/TemplateMod/$CLASS/g" \
		-e "s/template-mod/$ID/g" \
		"$f"
done

echo "created $ID/"
echo
echo "still to do by hand:"
echo "  1. replace the placeholder author and description in $ID/src/main/resources/fabric.mod.json"
